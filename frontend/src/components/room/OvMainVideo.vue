<template>
	<video autoplay/>
</template>

<script>
export default {
	name: 'OvMainVideo',

	props: {
		streamManager: Object,
	},

	mounted () {
		this.streamManager.addVideoElement(this.$el);
	},

	updated () {
		this.streamManager.addVideoElement(this.$el)
	}
};
</script>

<style scoped>
video {
  /* width: 859px; */
	width: 100%;
	/* height: 780px; */
	height: 73vh;
	padding: 40px 0px 10px;
  background-color: #D3E2E7;
}
</style>