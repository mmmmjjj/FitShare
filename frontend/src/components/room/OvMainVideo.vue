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
		this.streamManager.addVideoElement(this.$el);
	}
};
</script>

<style scoped>
video {
	width: 100%;
	height: 91vh;
	padding: 20px 0px 0;
  background-color: #D3E2E7;
}
</style>