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
	/* height: 780px; */
	width: 100%;
	height: 77.3vh;
	padding: 20px 0px 0;
  background-color: #D3E2E7;
}
</style>